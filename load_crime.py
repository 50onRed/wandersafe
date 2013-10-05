import csv
import json
import requests

def main(filename):
    with open(filename) as f:
        all_data = []
        reader = csv.reader(f)

        for line in reader:
            date = line[3]
            block = line[7]
            code = line[8]
            crime = line[10]
            lat = line[12]
            lon = line[13]

            headers = {
                    'X-StackMob-API-Key': '9f08750f-14b1-4e37-9867-9c54d7f4f0d5',
                    'Content-Type': 'application/json',
                    'Accept': 'application/vnd.stackmob+json; version=0'
                    }

            data = {
                    'dispatch_date': date,
                    'location_block': block,
                    'text_general_code': crime,
                    'ucr_general': int(code)
                    }

            if lat and lon:
                data.update({
                    'location': {
                        'lat': float(lat),
                        'lon': float(lon)
                        }
                    })

                all_data.append(data)

        response = requests.post('http://api.stackmob.com/crime', headers=headers,
                data=json.dumps(all_data))

        print response.text

if __name__ == '__main__':
    import sys
    main(sys.argv[1])
