"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
import psycopg2 as pg
from flask import jsonify
 
def transform(data):
    """transform query result to json"""
    newdict = {}
    newdict["TimeStamp"] = data[0].strftime('%Y-%m-%d %H:%M:%S')
    newdict["Case"] = data[1]
    newdict["ID"] = data[2]
    newdict["threshold"] = str(data[3])
    return newdict



@jidp.app.route('/api/otherstable/',methods=["GET"])
def get_others():
    directory = jidp.model.get_rainfall() # get the directory
    conn = pg.connect("dbname=anomalydb user=wangmk")
    cur = conn.cursor()
    standardfile = os.path.join(directory, "othersstandard.json")
    with open(standardfile, 'r') as f:
        standard = json.load(f)
    totalstandards = []
    if standard["brand"]:
        totalstandards.append("anomalytype='brand'")
    if standard["product"]:
        totalstandards.append("anomalytype='product'")
    if standard["category"]:
        totalstandards.append("anomalytype='category'")
    
    if not totalstandards:
        sql = "SELECT * FROM others ORDER BY inserttime DESC LIMIT 10;"
    else:
        statement = " OR ".join(totalstandards)
        sql = "SELECT * FROM others WHERE "+statement +"ORDER BY inserttime DESC LIMIT 10"
    cur.execute(sql)
    result = cur.fetchall()
    output = [transform(data) for data in result]
    return jsonify(output), 200
