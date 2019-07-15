"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
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
        with open(tempthreshold, "w+") as f:
            json.dump(feedback, f)
        output = {"status":"good"}
        return jsonify(output), 201
