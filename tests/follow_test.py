import requests
import json
import psycopg2

from imports import *
from wine_grades_test import add_wine, register

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