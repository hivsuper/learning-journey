import unittest

import util.fileUtil


class TestFileUtil(unittest.TestCase):

    def setUp(self):
        util.fileUtil.write('fileUtil.txt', '123\n456')

    def tearDown(self):
        util.fileUtil.delete('fileUtil.txt')

    def test_read_lines(self):
        self.assertListEqual(['123', '456'], util.fileUtil.read_lines('fileUtil.txt'))
