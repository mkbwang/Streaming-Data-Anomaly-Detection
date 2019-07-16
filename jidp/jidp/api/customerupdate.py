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
import urllib3
from flask import jsonify

@jidp.app.route('/api/customerupdate/',methods=["POST", "GET"])
def get_customerupdate():
    directory = jidp.model.get_rainfall()
    tempthreshold = os.path.join(directory, "customerthreshold.json")
    tempstandard = os.path.join(directory, "customerstandard.json")
    standard = {"userbrand":False, "userproduct": False, "usercategory": False}
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
        # store the new customer threshold in a temporary json file
        with open(tempthreshold, "w+") as f:
            json.dump(feedback, f)
        # TODO: add how to communicate with kafka
        # store the new standards
        with open(tempstandard, "w+") as f:
            json.dump(standard, f)
        if standard["userbrand"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 2, 'window' : 2000, 'threshold' : int(feedback["userbrand"])}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        if standard["userproduct"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 3, 'window' : 2000, 'threshold' : int(feedback["userproduct"])}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        if standard["usercategory"]:
            http = urllib3.PoolManager()
            data = {'type': 'update', 'ID' : 4, 'window' : 2000, 'threshold' : int(feedback["usercategory"])}
            encoded_data = json.dumps(data).encode('utf-8')
            r = http.request(
                'POST',
                '127.0.0.1:8080/api_v1/job',
                body=encoded_data,
                headers={'Content-Type': 'application/json'})
        output = {"status":"good"}
        return jsonify(output), 201
