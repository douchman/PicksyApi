import unittest
import yaml
import os


class TestCodeRabbitIntegration(unittest.TestCase):
    """
    Integration tests for .coderabbit.yaml configuration file.
    These tests validate the actual configuration file in the repository.
    """

    def test_integration_file_loads_correctly(self):
        """Integration test: Verify the actual .coderabbit.yaml file loads without errors."""
        config_path = '.coderabbit.yaml'
        
        # Skip if file doesn't exist (for CI environments)
        if not os.path.exists(config_path):
            self.skipTest(f"Configuration file {config_path} not found")
        
        with open(config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        self.assertIsNotNone(config, "Configuration should not be None")
        self.assertIsInstance(config, dict, "Configuration should be a dictionary")

    def test_integration_korean_encoding(self):
        """Integration test: Verify Korean characters are properly encoded."""
        config_path = '.coderabbit.yaml'
        
        if not os.path.exists(config_path):
            self.skipTest(f"Configuration file {config_path} not found")
        
        # Test reading with different encodings
        encodings_to_test = ['utf-8', 'utf-8-sig']
        
        for encoding in encodings_to_test:
            with self.subTest(encoding=encoding):
                try:
                    with open(config_path, 'r', encoding=encoding) as f:
                        content = f.read()
                    # Should not raise an exception
                    self.assertIsInstance(content, str)
                except UnicodeDecodeError:
                    if encoding == 'utf-8':
                        self.fail(f"File should be readable with {encoding} encoding")

    def test_integration_coderabbit_compatibility(self):
        """Integration test: Verify configuration is compatible with CodeRabbit expectations."""
        config_path = '.coderabbit.yaml'
        
        if not os.path.exists(config_path):
            self.skipTest(f"Configuration file {config_path} not found")
        
        with open(config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        # Test specific CodeRabbit compatibility requirements
        if 'reviews' in config and 'auto_review' in config['reviews']:
            auto_review = config['reviews']['auto_review']
            if auto_review.get('enabled', False):
                self.assertIn(
                    'target_branches', auto_review,
                    "When auto_review is enabled, target_branches should be specified"
                )
                target_branches = auto_review.get('target_branches', [])
                self.assertGreater(
                    len(target_branches), 0,
                    "At least one target branch should be specified when auto_review is enabled"
                )

    def test_integration_real_world_usage(self):
        """Integration test: Simulate real-world CodeRabbit usage scenarios."""
        config_path = '.coderabbit.yaml'
        
        if not os.path.exists(config_path):
            self.skipTest(f"Configuration file {config_path} not found")
        
        with open(config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        # Test that the configuration would work in practice
        reviews = config.get('reviews', {})
        
        # If high_level_summary is enabled, other review features should be properly configured
        if reviews.get('high_level_summary', False):
            self.assertIsInstance(reviews.get('profile'), str, "Profile should be set when using high_level_summary")
        
        # If request_changes_workflow is enabled, review_status should probably be enabled too
        if reviews.get('request_changes_workflow', False):
            self.assertTrue(
                reviews.get('review_status', False),
                "review_status should be enabled when using request_changes_workflow"
            )


if __name__ == '__main__':
    unittest.main()