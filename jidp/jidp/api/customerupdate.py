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
from flask import jsonify

@jidp.app.route('/api/customerupdate/',methods=["POST"])
def get_customerupdate():
    feedback = flask.request.get_json()
    for key in feedback:
        feedback[key] = int(feedback[key])
    # store the new customer threshold in a temporary pickle file
    directory = jidp.model.get_rainfall()
    tempfile = os.path.join(directory, "customerthreshold.pkl")
    with open(tempfile, "wb+") as f:
        pickle.dump(feedback, f)
    output = {"status":"good"}
    return jsonify(output), 201
