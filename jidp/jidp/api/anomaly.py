"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

@jidp.app.route('/api/anomaly/',methods=["GET"])
def get_anomaly():
    directory = jidp.model.get_rainfall() # get the directory
    filename = "example.npy"
    total_directory = os.path.join(directory, filename)
    mat = np.load(total_directory)
    # with open(total_directory, "rb") as img_file:
    #    imgstring = base64.b64encode(img_file.read())
    plt.figure(figsize=(4, 3))
    fig, ax = plt.subplots()
    im = ax.imshow(mat)
    # store it as a base64 stream and send it
    strIO = io.BytesIO()
    plt.savefig(strIO, format='png')
    strIO.seek(0)
    plt.close('all')
    plotcode = base64.encodestring(strIO.read())
    return plotcode.decode('utf-8'), 200, {'Content-Type': 'text/plain'}
