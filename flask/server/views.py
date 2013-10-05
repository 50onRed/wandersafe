from flask import request
from .stackmob import get_crime_near

def tell_me_if_im_going_to_die():
    return get_crime_near(request.args['latitude'],
                          request.args['longitude'],
                          request.args['radius'])