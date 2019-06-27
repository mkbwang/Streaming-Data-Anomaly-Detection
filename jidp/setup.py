"""JIDP python package configuration."""

from setuptools import setup

setup(
    name='jidp',
    version='0.1.0',
    packages=['jidp'],
    include_package_data=True,
    install_requires=[
        'Flask',
        'html5validator',
        'pycodestyle',
        'pydocstyle',
        'pylint',
        'arrow',
        'sh',
        'numpy',
        'matplotlib',
	'kafka-python'
    ],
)
