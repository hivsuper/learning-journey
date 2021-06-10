import os


def read_lines(filename):
    """read lines in a file"""
    try:
        with open(filename, 'r', encoding='utf-8') as f:
            contents = f.read()
    except FileNotFoundError:
        print(f'{filename} is not found')
    else:
        return contents.split('\n')


def write(filename, content):
    """write content to the file"""
    try:
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(content)
    finally:
        pass


def delete(filename):
    """delete the file"""
    if os.path.exists(filename):
        os.remove(filename)
    else:
        print(f'The file {filename} does not exist')
