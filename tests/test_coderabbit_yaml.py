import unittest
import yaml
import os
from typing import Dict, Any


class TestCodeRabbitYaml(unittest.TestCase):
    """
    Unit tests for .coderabbit.yaml configuration file.
    Testing framework: unittest (Python standard library)
    YAML library: PyYAML
    """

    def setUp(self):
        """Set up test fixtures before each test method."""
        self.config_file_path = '.coderabbit.yaml'
        self.expected_schema = {
            'language': str,
            'early_access': bool,
            'reviews': dict,
            'chat': dict
        }

    def tearDown(self):
        """Clean up after each test method."""
        pass

    def load_yaml_config(self) -> Dict[str, Any]:
        """Helper method to load and parse the YAML configuration."""
        try:
            with open(self.config_file_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f)
        except FileNotFoundError:
            self.fail(f"Configuration file {self.config_file_path} not found")
        except yaml.YAMLError as e:
            self.fail(f"Failed to parse YAML: {e}")

    def test_yaml_file_exists(self):
        """Test that the .coderabbit.yaml file exists."""
        self.assertTrue(
            os.path.exists(self.config_file_path),
            f"Configuration file {self.config_file_path} should exist"
        )

    def test_yaml_file_is_valid(self):
        """Test that the YAML file can be parsed without errors."""
        config = self.load_yaml_config()
        self.assertIsInstance(config, dict, "Configuration should be a dictionary")

    def test_required_top_level_keys(self):
        """Test that all required top-level keys are present."""
        config = self.load_yaml_config()
        required_keys = ['language', 'early_access', 'reviews', 'chat']
        
        for key in required_keys:
            with self.subTest(key=key):
                self.assertIn(key, config, f"Required key '{key}' should be present")

    def test_language_configuration(self):
        """Test language configuration is valid."""
        config = self.load_yaml_config()
        
        self.assertIn('language', config)
        self.assertIsInstance(config['language'], str)
        self.assertEqual(config['language'], 'ko-KR', "Language should be set to Korean")

    def test_early_access_configuration(self):
        """Test early_access configuration is valid."""
        config = self.load_yaml_config()
        
        self.assertIn('early_access', config)
        self.assertIsInstance(config['early_access'], bool)
        self.assertFalse(config['early_access'], "Early access should be disabled")

    def test_reviews_section_structure(self):
        """Test that the reviews section has the correct structure."""
        config = self.load_yaml_config()
        reviews = config.get('reviews', {})
        
        expected_reviews_keys = [
            'profile', 'request_changes_workflow', 'high_level_summary',
            'poem', 'review_status', 'collapse_walkthrough', 'auto_review'
        ]
        
        for key in expected_reviews_keys:
            with self.subTest(key=key):
                self.assertIn(key, reviews, f"Reviews should contain '{key}'")

    def test_reviews_profile_setting(self):
        """Test reviews profile configuration."""
        config = self.load_yaml_config()
        reviews = config.get('reviews', {})
        
        self.assertIn('profile', reviews)
        self.assertEqual(reviews['profile'], 'chill', "Profile should be set to 'chill'")

    def test_reviews_boolean_settings(self):
        """Test boolean settings in reviews section."""
        config = self.load_yaml_config()
        reviews = config.get('reviews', {})
        
        boolean_settings = {
            'request_changes_workflow': True,
            'high_level_summary': True,
            'poem': False,
            'review_status': True,
            'collapse_walkthrough': False
        }
        
        for setting, expected_value in boolean_settings.items():
            with self.subTest(setting=setting):
                self.assertIn(setting, reviews, f"Setting '{setting}' should be present")
                self.assertIsInstance(reviews[setting], bool, f"Setting '{setting}' should be boolean")
                self.assertEqual(
                    reviews[setting], expected_value,
                    f"Setting '{setting}' should be {expected_value}"
                )

    def test_auto_review_section(self):
        """Test auto_review subsection configuration."""
        config = self.load_yaml_config()
        reviews = config.get('reviews', {})
        auto_review = reviews.get('auto_review', {})
        
        # Test auto_review structure
        expected_auto_review_keys = ['enabled', 'drafts', 'target_branches']
        for key in expected_auto_review_keys:
            with self.subTest(key=key):
                self.assertIn(key, auto_review, f"auto_review should contain '{key}'")

    def test_auto_review_boolean_settings(self):
        """Test boolean settings in auto_review section."""
        config = self.load_yaml_config()
        auto_review = config.get('reviews', {}).get('auto_review', {})
        
        self.assertTrue(auto_review.get('enabled'), "Auto review should be enabled")
        self.assertFalse(auto_review.get('drafts'), "Draft PR review should be disabled")

    def test_target_branches_configuration(self):
        """Test target_branches configuration."""
        config = self.load_yaml_config()
        auto_review = config.get('reviews', {}).get('auto_review', {})
        target_branches = auto_review.get('target_branches', [])
        
        self.assertIsInstance(target_branches, list, "target_branches should be a list")
        self.assertIn('dev', target_branches, "target_branches should contain 'dev'")
        self.assertEqual(len(target_branches), 1, "Should have exactly one target branch")

    def test_chat_section(self):
        """Test chat section configuration."""
        config = self.load_yaml_config()
        chat = config.get('chat', {})
        
        self.assertIn('auto_reply', chat, "Chat section should contain 'auto_reply'")
        self.assertIsInstance(chat['auto_reply'], bool, "auto_reply should be boolean")
        self.assertTrue(chat['auto_reply'], "Auto reply should be enabled")

    def test_no_unexpected_top_level_keys(self):
        """Test that there are no unexpected top-level keys."""
        config = self.load_yaml_config()
        expected_keys = {'language', 'early_access', 'reviews', 'chat'}
        actual_keys = set(config.keys())
        
        unexpected_keys = actual_keys - expected_keys
        self.assertEqual(
            len(unexpected_keys), 0,
            f"Found unexpected top-level keys: {unexpected_keys}"
        )

    def test_yaml_format_consistency(self):
        """Test that YAML format is consistent and well-formed."""
        # Read raw file content to check formatting
        with open(self.config_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Check for consistent indentation (should be 2 spaces based on the file)
        lines = content.split('\n')
        for i, line in enumerate(lines, 1):
            # Check that indentation uses spaces, not tabs
            if line.strip() and not line.startswith('#') and '\t' in line:
                self.fail(f"Line {i} uses tabs instead of spaces for indentation")

    def test_configuration_values_are_sensible(self):
        """Test that configuration values make sense in context."""
        config = self.load_yaml_config()
        
        # Language should be a valid locale format
        language = config.get('language', '')
        self.assertRegex(
            language, r'^[a-z]{2}-[A-Z]{2}$',
            "Language should follow locale format (e.g., 'ko-KR')"
        )
        
        # Profile should be a known CodeRabbit profile
        profile = config.get('reviews', {}).get('profile', '')
        valid_profiles = ['chill', 'assertive', 'pythonic']  # Common CodeRabbit profiles
        self.assertIn(
            profile, valid_profiles,
            f"Profile '{profile}' should be one of {valid_profiles}"
        )

    def test_edge_case_empty_target_branches(self):
        """Test behavior when target_branches is empty (edge case)."""
        config = self.load_yaml_config()
        target_branches = config.get('reviews', {}).get('auto_review', {}).get('target_branches', [])
        
        # Should not be empty in current config, but test the structure
        if len(target_branches) == 0:
            self.fail("target_branches should not be empty if auto_review is enabled")

    def test_schema_validation(self):
        """Test comprehensive schema validation."""
        config = self.load_yaml_config()
        
        # Validate top-level structure
        self.assertIsInstance(config.get('language'), str)
        self.assertIsInstance(config.get('early_access'), bool)
        self.assertIsInstance(config.get('reviews'), dict)
        self.assertIsInstance(config.get('chat'), dict)
        
        # Validate reviews structure
        reviews = config.get('reviews', {})
        self.assertIsInstance(reviews.get('profile'), str)
        self.assertIsInstance(reviews.get('auto_review'), dict)
        
        # Validate auto_review structure
        auto_review = reviews.get('auto_review', {})
        self.assertIsInstance(auto_review.get('enabled'), bool)
        self.assertIsInstance(auto_review.get('drafts'), bool)
        self.assertIsInstance(auto_review.get('target_branches'), list)

    def test_korean_comments_preservation(self):
        """Test that Korean comments in the YAML file are preserved correctly."""
        with open(self.config_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Check that Korean characters are present in comments
        korean_patterns = ['한국어', '리뷰', '설정', '활성화', '비활성화']
        for pattern in korean_patterns:
            with self.subTest(pattern=pattern):
                self.assertIn(
                    pattern, content,
                    f"Korean comment '{pattern}' should be preserved in the file"
                )

    def test_yaml_load_with_different_loaders(self):
        """Test YAML loading with different loader types for security."""
        with open(self.config_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Test safe_load (recommended)
        try:
            config_safe = yaml.safe_load(content)
            self.assertIsInstance(config_safe, dict)
        except yaml.YAMLError:
            self.fail("YAML should be parseable with safe_load")
        
        # Test full_load for comparison
        try:
            config_full = yaml.full_load(content)
            self.assertEqual(config_safe, config_full, "safe_load and full_load should produce same result")
        except yaml.YAMLError:
            self.fail("YAML should be parseable with full_load")

    def test_branch_name_validation(self):
        """Test that target branch names are valid Git branch names."""
        config = self.load_yaml_config()
        target_branches = config.get('reviews', {}).get('auto_review', {}).get('target_branches', [])
        
        # Valid Git branch name pattern (simplified)
        import re
        valid_branch_pattern = re.compile(r'^[a-zA-Z0-9._/-]+$')
        
        for branch in target_branches:
            with self.subTest(branch=branch):
                self.assertIsInstance(branch, str, f"Branch name '{branch}' should be a string")
                self.assertTrue(
                    valid_branch_pattern.match(branch),
                    f"Branch name '{branch}' should be a valid Git branch name"
                )
                self.assertNotEqual(branch.strip(), '', "Branch name should not be empty or whitespace")

    def test_failure_condition_invalid_yaml_structure(self):
        """Test handling of invalid YAML structures (failure condition)."""
        # This test verifies error handling when YAML is malformed
        # We'll test this by temporarily creating an invalid config
        invalid_yaml_content = """
        language: "ko-KR"
        early_access: false
        reviews:
          profile: "chill"
          invalid_indentation:
        should_cause_error
        """
        
        temp_file = 'temp_invalid.yaml'
        try:
            with open(temp_file, 'w', encoding='utf-8') as f:
                f.write(invalid_yaml_content)
            
            with self.assertRaises(yaml.YAMLError), open(temp_file, 'r', encoding='utf-8') as f:
                yaml.safe_load(f)
        finally:
            if os.path.exists(temp_file):
                os.remove(temp_file)

    def test_edge_case_missing_nested_keys(self):
        """Test behavior when expected nested keys are missing (edge case)."""
        config = self.load_yaml_config()
        
        # Test graceful handling of potentially missing keys
        reviews = config.get('reviews', {})
        auto_review = reviews.get('auto_review', {})
        
        # These should exist in our config, but test the access pattern
        enabled = auto_review.get('enabled', False)
        drafts = auto_review.get('drafts', True)  # Default to True to be safe
        target_branches = auto_review.get('target_branches', [])
        
        self.assertIsInstance(enabled, bool)
        self.assertIsInstance(drafts, bool)
        self.assertIsInstance(target_branches, list)

    def test_unicode_encoding_edge_cases(self):
        """Test various Unicode encoding scenarios."""
        with open(self.config_file_path, 'rb') as f:
            raw_content = f.read()
        
        # Test different encoding scenarios
        encodings = ['utf-8', 'utf-8-sig']
        for encoding in encodings:
            with self.subTest(encoding=encoding):
                try:
                    decoded_content = raw_content.decode(encoding)
                    parsed_config = yaml.safe_load(decoded_content)
                    self.assertIsInstance(parsed_config, dict)
                except UnicodeDecodeError:
                    if encoding == 'utf-8':
                        self.fail(f"File should be decodable with {encoding}")

    def test_configuration_completeness(self):
        """Test that configuration is complete for CodeRabbit functionality."""
        config = self.load_yaml_config()
        
        # Check that auto_review is properly configured if enabled
        auto_review = config.get('reviews', {}).get('auto_review', {})
        if auto_review.get('enabled', False):
            self.assertIn('target_branches', auto_review, "Enabled auto_review requires target_branches")
            self.assertGreater(
                len(auto_review.get('target_branches', [])), 0,
                "At least one target branch must be specified"
            )
        
        # Check language is set for proper localization
        language = config.get('language')
        self.assertIsNotNone(language, "Language should be specified for proper localization")
        self.assertNotEqual(language.strip(), '', "Language should not be empty")


if __name__ == '__main__':
    unittest.main()