from flask import request
from .stackmob import get_crime_near


def tell_me_if_im_going_to_die():
    status= get_crime_near(float(request.args['latitude']),
                           float(request.args['longitude']),
                           float(request.args['meters']))
    return '{}'.format(status)