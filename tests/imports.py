import json
import psycopg2

HTTP_OK = 200
HTTP_FORBIDDEN = 403
HTTP_NOT_FOUND = 404


REGISTER_URL = 'http://localhost:8080/api/register'
WELCOME_URL = 'http://localhost:8080/api/welcome'
LOGOUT_URL = 'http://localhost:8080/api/user/logout'
LOGIN_URL = 'http://localhost:8080/api/login'

USER_ONLY_URL = 'http://localhost:8080/api/user/somerandompage'
REGULAR_USER_ONLY_URL = "http://localhost:8080/api/user/customer/otherpage"
SHOP_USER_ONLY_URL = "http://localhost:8080/api/user/store/yetanotherpage"
FORBIDDEN_URL = "http://localhost:8080/api/somerandomstuff"

ADD_WINE_URL = 'http://localhost:8080/api/user/addwine'

RATE_WINE_URL = 'http://localhost:8080/api/user/customer/ratewine'
AVG_RATE_URL = 'http://localhost:8080/api/user/averagerating'

FOLLOW_URL = 'http://localhost:8080/api/user/follow'
RECOMMENDATION_URL = 'http://localhost:8080/api/user/customer/recommendations'


def send_post_request(url, session, expected_code, payload=None):
    resp = session.post(url, data=json.dumps(payload, indent=4))
    assert resp.status_code == expected_code
    return resp.json()

def truncate_users():
    conn = psycopg2.connect(dbname="vineon", user="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM Users")
    cursor.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1")
    cursor.execute("DELETE FROM Customer")
    cursor.execute("DELETE FROM Store")
    conn.commit()

    return cursor


def truncate_wines():
    conn = psycopg2.connect(dbname="vineon", user="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM Wines")
    cursor.execute("ALTER SEQUENCE wines_id_seq RESTART WITH 1")
    conn.commit()
    return cursor


def truncate_grades():
    conn = psycopg2.connect(dbname="vineon", user="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM wine_grades")
    cursor.execute("ALTER SEQUENCE wine_grades_id_seq RESTART WITH 1")
    conn.commit()
    return cursor


def truncate_follows():
    conn = psycopg2.connect(dbname="vineon", user="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM followers")
    cursor.execute("ALTER SEQUENCE followers_id_seq RESTART WITH 1")
    conn.commit()
    return cursor


def register(s, username, role):
    register_payload = {"params": {
        "username": username,
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }}

    resp = send_post_request(REGISTER_URL, s, HTTP_OK, register_payload)
    return resp