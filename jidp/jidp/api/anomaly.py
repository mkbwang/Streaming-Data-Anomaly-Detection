"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
from datetime import datetime
from datetime import timedelta
import psycopg2 as pg
from flask import jsonify

@jidp.app.route('/api/anomaly/',methods=["GET"])
def get_anomaly():
    conn = pg.connect("dbname=anomalydb user=wmk")
    cur = conn.cursor()
    nowtime = datetime.now()
    previoustime = nowtime - timedelta(seconds=2.3)
    timerestraint = "(inserttime BETWEEN '"+previoustime.strftime("%Y-%m-%d %H:%M:%S")+"' AND '"+nowtime.strftime("%Y-%m-%d %H:%M:%S")+"')"
    querystring1 = "SELECT * FROM rainfall WHERE (anomalytype = 'threshold') AND "+timerestraint+" ORDER BY value DESC LIMIT 200"
    querystring2 = "SELECT * FROM rainfall WHERE (anomalytype = 'statistical') AND "+timerestraint+" ORDER BY value DESC LIMIT 200"
    cur.execute(querystring1)
    threshold = cur.fetchall()
    a1 = [data[2] for data in threshold]
    cur.execute(querystring2)
    statistical = cur.fetchall()
    a2 = [data[2] for data in statistical]
    cur.close()
    conn.close()
    # directory = jidp.model.get_rainfall() # get the directory
    # a1 = "a1.json"
    # a1_directory = os.path.join(directory, a1)
    # with open(a1_directory, 'r') as f:
    #     anomaly1 = json.load(f)
    # a2 = "a2.json"
    # a2_directory = os.path.join(directory, a2)
    # with open(a2_directory, 'r') as f:
    #     anomaly2 = json.load(f)
    output = {"a1": a1, "a2": a2}
    return jsonify(output), 200
