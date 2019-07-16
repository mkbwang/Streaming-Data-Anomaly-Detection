"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
import pickle
from datetime import datetime
from datetime import timedelta
import psycopg2 as pg
from flask import jsonify

def transform(data):
    """transform query result to json"""
    newdict = {}
    newdict["TimeStamp"] = data[0].strftime('%Y-%m-%d %H:%M:%S')
    newdict["AnomalyReason"] = data[1]
    newdict["UserID"] = data[2]
    newdict["threshold"] = str(data[3])
    newdict["value"] = str(data[4])
    return newdict


@jidp.app.route('/api/customer/',methods=["GET"])
def get_customer():
    directory = jidp.model.get_rainfall() # get the directory
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    standardfile = os.path.join(directory, "customerstandard.json")
    with open(standardfile, 'r') as f:
        standard = json.load(f)
    totalstandards = []
    if standard["userbrand"]:
        totalstandards.append("anomalytype='brand'")
    if standard["userproduct"]:
        totalstandards.append("anomalytype='product'")
    if standard["usercategory"]:
        totalstandards.append("anomalytype='category'")
    if not totalstandards:
        sql = "SELECT * FROM customer ORDER BY inserttime DESC LIMIT 10;"
    else:
        statement = " OR ".join(totalstandards)
        sql = "SELECT * FROM customer WHERE "+statement +"ORDER BY inserttime DESC LIMIT 10"
    cur.execute(sql)
    result = cur.fetchall()
    output = [transform(data) for data in result]
    # print(output[0])
    return jsonify(output), 200
