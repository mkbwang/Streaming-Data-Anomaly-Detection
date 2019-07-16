"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
import urllib3
from flask import jsonify

@jidp.app.route('/api/rainupdate/',methods=["POST", "GET"])
def get_rainupdate():
    directory = jidp.model.get_rainfall()
    tempthreshold = os.path.join(directory, "rainthreshold.json")
    if flask.request.method=="GET":
        # this is meant for setting the threshold value when the user first open the site
        with open(tempthreshold, 'r') as f:
            output = json.load(f)
        return jsonify(output), 200
    else:
        # update the rain threshold
        http = urllib3.PoolManager()
        feedback = flask.request.get_json()
        data = {'type':'update', 'ID':0, 'window':2000, "threshold":int(feedback["threshold"])}
        encoded_data = json.dumps(data).encode('utf-8')
        r = http.request(
            'POST',
            '127.0.0.1:8080/api_v1/job',
            body=encoded_data,
            headers={'Content-Type': 'application/json'})
        output = {"status":"good"}
        return jsonify(output), 201
