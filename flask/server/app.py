from flask import Flask
from .views import tell_me_if_im_going_to_die, show_map

def create_app():
    app = Flask(__name__)
    app.config.from_pyfile('config_default.py')
    app.add_url_rule('/', 'tell_me_if_im_going_to_die', tell_me_if_im_going_to_die)
    app.add_url_rule('/map', 'show_map', show_map)
    return app
