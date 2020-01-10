import requests
import json
import psycopg2

from imports import *
from wine_grades_test import add_wine, register, grade_wine

def test_basic_follow():
    s = requests.Session()
    cursor = truncate_users()
    truncate_wines()
    truncate_grades()
    truncate_follows()

    username = "sampleUsername"
    role = "customer"
    resp = register(s, username, role)
    assert resp['success'] is True

    logout_resp = send_post_request(LOGOUT_URL, s, HTTP_OK)
    assert logout_resp['success'] is True

    username = "sampleUsername2"
    role = "customer"
    resp = register(s, username, role)
    assert resp['success'] is True

    payload = {'params': {'username': "sampleUsername"}}
    follow_resp = send_post_request(FOLLOW_URL, s, HTTP_OK, payload)
    assert follow_resp == "ABC"


def test_recommendations():
    s = requests.Session()
    cursor = truncate_users()
    truncate_wines()
    truncate_grades()
    truncate_follows()

    username = "sampleUsername"
    role = "customer"
    resp = register(s, username, role)
    assert resp['success'] is True

    wine1 = add_wine(s, 'wine1')
    assert wine1['success'] is True

    wine2 = add_wine(s, 'wine2')
    assert wine2['success'] is True

    wine3 = add_wine(s, 'wine3')
    assert wine3['success'] is True

    rate1 = grade_wine(s, 5, 'wine1')
    assert rate1['success'] is True
    rate2 = grade_wine(s, 1, 'wine2')
    assert rate2['success'] is True
    rate3 = grade_wine(s, 8, 'wine3')
    assert rate3['success'] is True

    logout_resp = send_post_request(LOGOUT_URL, s, HTTP_OK)
    assert logout_resp['success'] is True

    username = "sampleUsername2"
    role = "customer"
    resp = register(s, username, role)
    assert resp['success'] is True

    rate1 = grade_wine(s, 7, 'wine1')
    assert rate1['success'] is True
    rate2 = grade_wine(s, 3, 'wine2')
    assert rate2['success'] is True
    rate3 = grade_wine(s, 6, 'wine3')
    assert rate3['success'] is True

    logout_resp = send_post_request(LOGOUT_URL, s, HTTP_OK)
    assert logout_resp['success'] is True

    username = "sampleUsername3"
    role = "customer"
    resp = register(s, username, role)
    assert resp['success'] is True

    test_resp = send_post_request(RECOMMENDATION_URL, s, HTTP_OK)
    assert  test_resp=="AB"


