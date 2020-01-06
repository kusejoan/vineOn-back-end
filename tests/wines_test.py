import requests
import json
import psycopg2

from imports import *
from wine_grades_test import add_wine, register


def send_post_request(url, session, expected_code, payload=None):
    resp = session.post(url, data=json.dumps(payload, indent=4))
    assert resp.status_code == expected_code
    return resp.json()


def test_adding_wine():
    """
    This test does the following (on each step also return jsons are checked)
    1) Registers user
    2) Adds first wine to database
    3) Checks if one wine is now in database
    4) Tries to add the same wine to database once more
    5) Checks that still only one wine is in database
    6) Adds another wine to database
    7) Checks that now there are two wines in database
    8) Adds wine without field wineName
    9) Checks that this wine was not added
    """
    s = requests.Session()
    cursor = truncate_users()
    truncate_wines()
    # 1
    username = "sampleUsername"
    role = "store"
    resp = register(s, username, role)
    assert resp['success'] is True
    # 2
    wineName = "wino1"
    add_wine_resp = add_wine(s, wineName)
    assert add_wine_resp['success'] is True
    assert add_wine_resp['message'] == "Wine "+wineName+" added to database"
    # 3
    cursor.execute("SELECT COUNT (*) from wines")
    count = cursor.fetchone()[0]
    assert count == 1
    cursor.execute("SELECT * FROM wines")
    row = cursor.fetchone()
    assert row[1] == wineName
    # 4
    add_wine_resp2 = add_wine(s, wineName)
    assert add_wine_resp2['success'] is False
    assert add_wine_resp2['message'] == "Wine " + wineName + " already is in database"
    # 5
    cursor.execute("SELECT COUNT (*) from wines")
    count = cursor.fetchone()[0]
    assert count == 1
    # 6
    wineName = "wino2"
    add_wine_resp = add_wine(s, wineName)
    assert add_wine_resp['success'] is True
    assert add_wine_resp['message'] == "Wine " + wineName + " added to database"
    # 7
    cursor.execute("SELECT COUNT (*) from wines")
    count = cursor.fetchone()[0]
    assert count == 2
    # 8
    invalid_payload = {'params': {
        "randomField": "randomValue"}
    }
    bad_add_wine_resp = send_post_request(ADD_WINE_URL, s, HTTP_OK, invalid_payload)
    assert bad_add_wine_resp['success'] is False
    assert bad_add_wine_resp['message'] == 'No value for wineName'
    # 9
    cursor.execute("SELECT COUNT (*) from wines")
    count = cursor.fetchone()[0]
    assert count == 2
    truncate_users()
    truncate_wines()
