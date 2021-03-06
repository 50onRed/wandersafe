from flask import request, render_template, jsonify, redirect, url_for
from .stackmob import get_crime_near
import json

DEBUG_LEVEL = None

def index():
    return render_template("index.html")

def tell_me_if_im_going_to_die(lat, lon, meters):
    try:
        import uwsgi
        DEBUG_LEVEL = uwsgi.cache_get("DEBUG")
        if DEBUG_LEVEL is not None:
            return DEBUG_LEVEL
    except ImportError:
        pass
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    level = process_crime_level(response)
    return str(level)

def get_crimes(lat, lon, meters=None):
    meters = meters or 5000
    lat, lon, meters = float(lat), float(lon), float(meters)
    l = get_crime_near(lat, lon, meters)
    return json.dumps(l)

def show_map(lat, lon):
    lat, lon = float(lat), float(lon)
    return render_template("map.html", lat=lat, lon=lon)

def process_crime_level(crimes):
    if len(crimes) == 0:
        return 0 # avoid division by zero
    sum_of_distances = sum([float(crime['location']['distance']) for crime in crimes])
    total = sum([weight_crime_level(crime, sum_of_distances) for crime in crimes])
    new_sum_of_distances = sum([sum_of_distances - float(crime['location']['distance']) for crime in crimes])
    return 8 - int(total / 100.0 / new_sum_of_distances)

def reset_debug_level(level):
    import uwsgi
    if level == '-1':
        uwsgi.cache_del("DEBUG")
    else:
        if uwsgi.cache_exists("DEBUG"):
            uwsgi.cache_update("DEBUG", level)
        else:
            uwsgi.cache_set("DEBUG", level)
    return redirect(url_for('tell_me_if_im_going_to_die', lat=39.9708657, lon=-75.1427425, meters=1000))

def weight_crime_level(crime, sum_of_distances):
    distance = float(crime['location']['distance'])
    print('Distance: {}'.format(distance))
    print('UCR: {}'.format(float(crime['ucr_general'])))
    print('Mean: {}'.format(float(crime['ucr_general']) * (sum_of_distances - distance)))
    return float(crime['ucr_general']) * (sum_of_distances - distance)
