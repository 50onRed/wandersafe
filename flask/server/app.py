from flask import Flask
from .views import tell_me_if_im_going_to_die

def create_app():
    app = Flask(__name__)
    app.config.from_pyfile('config_default.py')
    app.add_url_rule('/', 'tell_me_if_im_going_to_die', tell_me_if_im_going_to_die)
    return app

if __name__ == '__main__':
    app = create_app()
    app.run()