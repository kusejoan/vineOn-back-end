import requests
import json
import psycopg2

HTTP_OK = 200
HTTP_FORBIDDEN = 403
HTTP_NOT_FOUND = 404
REGISTER_URL = 'http://localhost:8080/api/register'
WELCOME_URL = 'http://localhost:8080/api/welcome'
LOGOUT_URL = 'http://localhost:8080/api/username/logout'
LOGIN_URL = 'http://localhost:8080/api/login'

USER_ONLY_URL = 'http://localhost:8080/api/username/somerandompage'
REGULAR_USER_ONLY_URL = "http://localhost:8080/api/username/regular/otherpage"
SHOP_USER_ONLY_URL = "http://localhost:8080/api/username/shop/yetanotherpage"
FORBIDDEN_URL = "http://localhost:8080/api/somerandomstuff"



def truncate_users():
    conn = psycopg2.connect(dbname="vineon", username="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM Users")
    cursor.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1")
    cursor.execute("DELETE FROM Customer")
    cursor.execute("DELETE FROM Store")
    conn.commit()

    return cursor


def send_post_request(url, session, expected_code, payload=None):
    resp = session.post(url, data=json.dumps(payload, indent=4))
    assert resp.status_code == expected_code
    return resp.json()


def test_basic_registration():
    """
    This test does the following
    1) Register username
    2) Check if users is successfully registered into db
    3) Try to register the same username again
    4) Check that username wasn't registered because it already exists
    5) Try to register new username but with invalid credentials (too short username)
    6) Check that username wasn't registered because username is too short
    """
    cursor = truncate_users()
    session = requests.Session()

    username = "sampleUsername"
    role = "customer"

    payload = {
        "username": username,
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }

    resp_body = send_post_request(REGISTER_URL, session, HTTP_OK, payload)

    # Validate
    assert resp_body['success'] is True
    assert resp_body['username'] == username
    assert resp_body['role'] == role
    cursor.execute("SELECT COUNT (*) from users")
    count = cursor.fetchone()[0]
    assert count == 1

    # second attempt of adding the same username
    resp_body = send_post_request(REGISTER_URL, session, HTTP_OK, payload)

    assert resp_body['success'] is False
    assert resp_body['username'] is None
    assert resp_body['message'] == 'User already exists'
    cursor.execute("SELECT COUNT (*) from users")
    count = cursor.fetchone()[0]
    assert count == 1

    # invalid credentials username
    payload = {
        "username": "short",
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }
    resp_body = send_post_request(REGISTER_URL, session, HTTP_OK, payload)

    assert resp_body['success'] is False
    assert resp_body['username'] is None
    assert resp_body['message'] == "Username must be between 6 and 32 characters"
    truncate_users()


def test_registration_logout_and_login():
    """
    This test does the following:
    1) Register new username
    2) Check if username is logged
    3) Logout username
    4) Check if no username is logged
    5) Login into previously registered account
    6) Check if username is logged again

    """
    truncate_users()
    session = requests.Session()

    username = "sampleUsername"
    role = "customer"
    register_payload = {
        "username": username,
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }

    send_post_request(REGISTER_URL, session, HTTP_OK, register_payload)

    welcome_resp = send_post_request(WELCOME_URL, session, HTTP_OK)

    assert welcome_resp['username'] == username
    assert welcome_resp['success'] is True

    logout_resp = send_post_request(LOGOUT_URL, session, HTTP_OK)

    assert logout_resp['success'] is True

    welcome_resp = send_post_request(WELCOME_URL, session, HTTP_OK)
    assert welcome_resp['success'] is False

    login_payload = {
        "username": username,
        "password": "PASSWORD"
    }

    login_resp = send_post_request(LOGIN_URL, session, HTTP_OK, login_payload)

    assert login_resp['username'] == username
    assert login_resp['success'] is True

    welcome_resp = send_post_request(WELCOME_URL, session, HTTP_OK)

    assert welcome_resp['username'] == username
    assert welcome_resp['success'] is True


def test_user_permissions():
    cursor = truncate_users()
    session = requests.session()

    register_payload = {
        "username": "regularUser",
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": "regular"
    }
    send_post_request(REGISTER_URL, session, HTTP_OK, register_payload)
    send_post_request(LOGOUT_URL, session, HTTP_OK)

    register_payload = {
        "username": "shopUser",
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": "shop"
    }
    send_post_request(REGISTER_URL, session, HTTP_OK, register_payload)
    send_post_request(LOGOUT_URL, session, HTTP_OK)

    cursor.execute("SELECT COUNT (*) from users")
    count = cursor.fetchone()[0]
    assert count == 2

    send_post_request(USER_ONLY_URL, session, HTTP_FORBIDDEN)
    send_post_request(REGULAR_USER_ONLY_URL, session, HTTP_FORBIDDEN)
    send_post_request(SHOP_USER_ONLY_URL, session, HTTP_FORBIDDEN)

    shop_login_payload = {
        "username": "shopUser",
        "password": "PASSWORD"
    }

    send_post_request(LOGIN_URL, session, HTTP_OK, shop_login_payload)
    send_post_request(USER_ONLY_URL, session, HTTP_NOT_FOUND)
    send_post_request(REGULAR_USER_ONLY_URL, session, HTTP_FORBIDDEN)
    send_post_request(SHOP_USER_ONLY_URL, session, HTTP_NOT_FOUND)
    send_post_request(LOGOUT_URL, session, HTTP_OK)

    regular_login_payload = {
        "username": "regularUser",
        "password": "PASSWORD"
    }

    send_post_request(LOGIN_URL, session, HTTP_OK, regular_login_payload)
    send_post_request(USER_ONLY_URL, session, HTTP_NOT_FOUND)
    send_post_request(REGULAR_USER_ONLY_URL, session, HTTP_NOT_FOUND)
    send_post_request(SHOP_USER_ONLY_URL, session, HTTP_FORBIDDEN)
    send_post_request(LOGOUT_URL, session, HTTP_OK)

    send_post_request(FORBIDDEN_URL,session,HTTP_FORBIDDEN)



test_basic_registration()
