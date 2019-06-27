import os

# Root of this application, useful if it doesn't occupy an entire domain
APPLICATION_ROOT = '/'

# rainfall matrix is in data/array_10.npy
RAINFALL_FILENAME = os.path.join(
    os.path.dirname(os.path.dirname(os.path.realpath(__file__))),
    'temporary'
)
