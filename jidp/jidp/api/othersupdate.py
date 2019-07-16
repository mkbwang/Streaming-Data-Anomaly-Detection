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

@jidp.app.route('/api/othersupdate/',methods=["POST", "GET"])
def get_othersupdate():
    directory = jidp.model.get_rainfall()
    tempthreshold = os.path.join(directory, "othersthreshold.json")
    tempstandard = os.path.join(directory, "othersstandard.json")
    standard = {"brand":False, "product": False, "category": False}
    # update the standards that the user suggests
    if flask.request.method=="GET":
        # this is meant for setting the threshold value when the user first open the site
        initialstate = {}
        with open(tempthreshold, 'r') as f:
            initialstate["threshold"] = json.load(f)
        with open(tempstandard, 'r') as f:
            initialstate["standard"] = json.load(f)
        return jsonify(initialstate), 200
    else: # post request, user change threshold
        feedback = flask.request.get_json()
        for key in feedback:
            feedback[key] = int(feedback[key])
            standard[key] = True
        # store the new others threshold in a temporary json file
        with open(tempthreshold, "w+") as f:
            json.dump(feedback, f)
        # TODO: add how to communicate with kafka
        # store the new standards
        with open(tempstandard, "w+") as f:
            json.dump(standard, f)
        if standard["brand"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 5, 'window' : 2000, 'threshold' : feedback["brand"]}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        if standard["product"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 6, 'window' : 2000, 'threshold' : feedback["product"]}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        if standard["category"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 7, 'window' : 2000, 'threshold' : feedback["category"]}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        output = {"status":"good"}
        return jsonify(output), 201
