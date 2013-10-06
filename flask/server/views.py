from flask import request, render_template
from .stackmob import get_crime_near

import logging
logging.basicConfig(level=logging.DEBUG)

def index():
    return render_template("index.html")

def tell_me_if_im_going_to_die(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    level = process_crime_level(response)
    return str(level)

def show_map(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    return render_template("map.html", lat=lat, lon=lon, crimes=response)

def process_crime_level(crimes):
    # logging.debug(crimes)
    if len(crimes) == 0:
        return 0 # avoid division by zero
    sum_of_distances = sum([float(crime['location']['distance']) for crime in crimes])
    total = sum([float(crime['ucr_general']) for crime in crimes])
    return 8 - int(total / 100.00 / len(crimes))

def weight_crime_level(crime, sum_of_distances, num_of_crimes):
    distance = float(crime['location']['distance'])
    logging.debug(type(distance))
    return (float(crime['ucr_general'])/num_of_crimes)*((sum_of_distances - distance)/sum_of_distances)