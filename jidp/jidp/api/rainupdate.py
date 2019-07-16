"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
import urllib.request as urllib2
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
        feedback = flask.request.get_json()
        feedback["topic"] = "weatherdata"
        feedback["window"] = 3000
        feedback["type"] = "anomaly_0"
        with open(tempthreshold, "w+") as f:
            json.dump(feedback, f)
        headers = {'Content-Type': 'application/json'}
        request = urllib2.Request(url='localhost:8080/api_v1/job/', headers=headers, data=json.dumps(feedback))
        response = urllib2.urlopen(request)
        output = {"status":"good"}
        return jsonify(output), 201
