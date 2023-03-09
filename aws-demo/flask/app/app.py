from logging.config import dictConfig

import boto3
from flask import Flask

app = Flask(__name__)
dictConfig({
    'version': 1,
    'formatters': {
        'console_fmt': {
            'format': '%(asctime)s %(levelname)s service_name="' + __name__ + '" ' +
                      ' class="%(name)s" event_description="%(message)s"'
        }
    },
    'handlers': {
        'console': {
            'class': 'logging.StreamHandler',
            'formatter': 'console_fmt',
            'stream': 'ext://sys.stdout',
            'level': 'INFO',
        }
    },
    'root': {
        'level': 'INFO',
        'handlers': ['console']
    },
    'disable_existing_loggers': False
})


@app.route('/', methods=['GET'])
def health_check():
    app.logger.info("call health_check")
    return "Welcome!"


@app.route('/s3-bucket/<name>', methods=['POST'])
def create_s3_bucket(name):
    app.logger.info("create %s", name)
    s3 = boto3.resource('s3')
    s3.create_bucket(Bucket=name)
    return name


@app.route('/s3-bucket/<name>', methods=['GET'])
def get_s3_bucket(name):
    app.logger.info("get %s", name)
    s3 = boto3.resource('s3')
    return s3.Bucket(name).creation_date


@app.route('/s3-bucket/<name>', methods=['DELETE'])
def delete_s3_bucket(name):
    app.logger.info("delete %s", name)
    s3 = boto3.resource('s3')
    s3.delete_bucket(Bucket=name)
    return name


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
