import requests
import json
import psycopg2

from imports import *


def add_wine(s, wineName):
    add_wine_payload = {"params": {
        "wineName": wineName,
        "country": "Poland",
        "color": "Red",
        "type": "Sweet",
        "year": 1993,
        "description": "Opis wina 1"
    }}

    add_wine_resp = send_post_request(ADD_WINE_URL, s, HTTP_OK, add_wine_payload)
    return add_wine_resp


def grade_wine(s, grade, wineName):
    grade_wine_payload = {"params": {
        "wineName": wineName,
        "grade": grade,
        "description": "Opis wina 1"
    }}

    wine_grade_resp = send_post_request(RATE_WINE_URL, s, HTTP_OK, grade_wine_payload)
    return wine_grade_resp


def test_basic_rating():
    """
    This test does the following (on each step also return jsons are checked)
    1) Register user
    2) Adds wine to database
    3) Rates this wine
    4) Checks if rating is saved in database
    5) Rates the same wine once more
    6) Checks if rating is modified in database (no new record!)
    7) Tries to rate wine that is not in database
    8) Checks that no record was added to database
    9) Adds second wine to database
    10) Rates the second wine
    11) Checks that now there are two records in database (one for each wine)

    """
    s = requests.Session()
    cursor = truncate_users()
    truncate_wines()
    truncate_grades()

    username = "sampleUsername"
    role = "customer"

    register_payload = {"params": {
        "username": username,
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }}
    # 1
    resp = send_post_request(REGISTER_URL, s, HTTP_OK, register_payload)
    assert resp['success'] is True
    # 2
    wineName = "wino1"
    add_wine_resp = add_wine(s, wineName)
    assert add_wine_resp['success'] is True
    assert add_wine_resp['message'] == "Wine " + wineName + " added to database"
    # 3
    grade = 7
    wine_grade_resp = grade_wine(s, grade, wineName)
    assert wine_grade_resp['success'] is True
    assert wine_grade_resp['message'] == "Wine " + wineName + " successfully rated"
    # 4
    cursor.execute("SELECT COUNT (*) from wine_grades")
    count = cursor.fetchone()[0]
    assert count == 1
    cursor.execute("SELECT * FROM wine_grades")
    row = cursor.fetchone()
    grade_id = row[0]
    assert row[3] == grade
    # 5
    grade = 6
    wine_grade_resp = grade_wine(s, grade, wineName)
    assert wine_grade_resp['success'] is True
    assert wine_grade_resp['message'] == "Rating for " + wineName + " successfully changed"
    # 6
    cursor.execute("SELECT COUNT (*) from wine_grades")
    count = cursor.fetchone()[0]
    assert count == 1
    cursor.execute("SELECT * FROM wine_grades")
    row = cursor.fetchone()
    assert row[3] == grade
    assert grade_id == row[0]

    wineName = "wino2"
    # 7
    wine_grade_resp = grade_wine(s, grade, wineName)
    assert wine_grade_resp['success'] is False
    assert wine_grade_resp['message'] == "Wine " + wineName + " not found in database"
    # 8
    cursor.execute("SELECT COUNT (*) from wine_grades")
    count = cursor.fetchone()[0]
    assert count == 1

    # 9
    wineName = "wino3"
    add_wine_resp = add_wine(s, wineName)

    assert add_wine_resp['success'] is True
    # 10
    grade = 5
    wine_grade_resp = grade_wine(s, grade, wineName)

    assert wine_grade_resp['success'] is True
    assert wine_grade_resp['message'] == "Wine " + wineName + " successfully rated"
    # 11
    cursor.execute("SELECT COUNT (*) from wine_grades")
    count = cursor.fetchone()[0]
    assert count == 2
    cursor.execute("SELECT * FROM wine_grades")
    second_row = cursor.fetchall()[1]
    assert second_row[3] == grade

    truncate_users()
    truncate_wines()
    truncate_grades()


def test_average_rating():
    """
    This test does the following: (on each step also return jsons are checked)
    1) Registers user
    2) Gets average rating for non-existing wine
    3) Adds wine to database
    4) Gets average rating for wine with no ratings
    5) Rates wine
    6) Checks that average rating is now equal to rating from 5)
    7) Logs out
    8) Registers second user
    9) Rates wine once more (from different user's account)
    10) Checks that average rating is calculated properly
    """
    s = requests.Session()
    truncate_users()
    truncate_wines()
    truncate_grades()
    # 1
    register_resp = register(s, "sampleUsername", "customer")
    assert register_resp['success'] is True
    # 2
    wineName = "wino1"
    average_rating_payload = {'params': {
        'wineName': wineName
    }}
    avg_resp = send_post_request(AVG_RATE_URL, s, HTTP_OK, average_rating_payload)
    assert avg_resp['success'] is False
    assert avg_resp['grade'] == -1
    assert avg_resp['amountOfGrades'] == -1
    assert avg_resp['message'] == "Wine "+wineName+" not found in database"
    # 3
    add_wine_resp = add_wine(s, wineName)
    assert add_wine_resp['success'] is True
    # 4
    avg_resp = send_post_request(AVG_RATE_URL, s, HTTP_OK, average_rating_payload)
    assert avg_resp['success'] is True
    assert avg_resp['grade'] == 0
    assert avg_resp['amountOfGrades'] == 0
    assert avg_resp['message'] == "No ratings yet"
    # 5
    grade = 5
    wine_grade_resp = grade_wine(s, grade, wineName)
    assert wine_grade_resp['success'] is True
    # 6
    avg_resp = send_post_request(AVG_RATE_URL, s, HTTP_OK, average_rating_payload)
    assert avg_resp['success'] is True
    assert avg_resp['grade'] == grade
    assert avg_resp['amountOfGrades'] == 1
    assert avg_resp['message'] == "OK"
    # 7
    logout_resp = send_post_request(LOGOUT_URL, s, HTTP_OK)
    assert logout_resp['success'] is True
    # 8
    register_resp = register(s, "sampleUsername2", "customer")
    assert register_resp['success'] is True
    # 9
    grade2 = 6
    wine_grade_resp = grade_wine(s, grade2, wineName)
    assert wine_grade_resp['success'] is True
    # 10
    avg_resp = send_post_request(AVG_RATE_URL, s, HTTP_OK, average_rating_payload)
    assert avg_resp['success'] is True
    assert avg_resp['grade'] == (grade+grade2)/2
    assert avg_resp['amountOfGrades'] == 2
    assert avg_resp['message'] == "OK"
