import unittest
import sys
import importlib.util


class TestRequiredDependencies(unittest.TestCase):
    """Test that required dependencies for YAML testing are available."""
    
    def test_yaml_library_available(self):
        """Test that PyYAML library is available."""
        spec = importlib.util.find_spec('yaml')
        self.assertIsNotNone(
            spec,
            "PyYAML library is required but not available. Install with: pip install PyYAML"
        )
    
    def test_yaml_safe_load_available(self):
        """Test that yaml.safe_load function is available."""
        import yaml
        self.assertTrue(hasattr(yaml, 'safe_load'), "yaml.safe_load should be available")
    
    def test_python_version_compatible(self):
        """Test that Python version is compatible with our tests."""
        version_info = sys.version_info
        self.assertGreaterEqual(
            version_info.major, 3,
            "Python 3.x is required for these tests"
        )


if __name__ == '__main__':
    unittest.main()