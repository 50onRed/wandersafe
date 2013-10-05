from flask import Flask
from .views import wandersafe

def create_app():
    app = Flask(__name__)
    app.config.from_pyfile('config_default.py')
    app.register_blueprint(wandersafe)
    return app