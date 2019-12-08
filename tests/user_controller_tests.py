import requests
import json
import psycopg2


def truncate_database():
    conn = psycopg2.connect(dbname="vineon", user="postgres", password="postgres")
    cursor = conn.cursor()
    cursor.execute("Truncate table users")
    cursor.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1")
    conn.commit()


def test_post_headers_body_json():
    truncate_database()
    url = 'http://localhost:8080/api/register'

    # Additional headers.
    headers = {'Content-Type': 'application/json'}

    username = "sampleUsername"
    role = "regular"
    # Body
    payload = {
        "username": username,
        "password": "PASSWORD",
        "passwordConfirm": "PASSWORD",
        "role": role
    }

    # convert dict to json by json.dumps() for body data.
    resp = requests.post(url, data=json.dumps(payload, indent=4))

    # print response full body as text
    print(resp.text)

    # Validate response headers and body contents, e.g. status code.
    assert resp.status_code == 200
    resp_body = resp.json()
    assert resp_body['success'] == 1




