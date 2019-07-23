"""
JIDP index (main) view.
URLs include:
/
"""
import flask
from flask import request
from flask import session
from flask import redirect
from flask import url_for
import numpy as np
import os
import matplotlib.pyplot as plt
import jidp


@jidp.app.route('/', methods=['GET'])
def show_index():
    """Display / route."""
    return flask.render_template("index.html")

@jidp.app.route('/index', methods=['GET'])
def index():
    """Display / route."""
    return flask.render_template("index.html")

@jidp.app.route('/taobao', methods=['GET'])
def taobao():
    """Display / route."""
    return flask.render_template("taobao.html")