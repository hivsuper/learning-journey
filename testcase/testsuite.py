import unittest

from fileUtil_test import TestFileUtil

if __name__ == '__main__':
    suite = unittest.TestSuite()
    suite.addTest(TestFileUtil('test_read_lines'))
    runner = unittest.TextTestRunner(verbosity=2)
    runner.run(suite)
