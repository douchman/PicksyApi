import unittest
import yaml
import os
from typing import Any, Dict, List


class TestYamlSchemaValidation(unittest.TestCase):
    """
    Schema validation tests for .coderabbit.yaml configuration file.
    Testing framework: unittest (Python standard library)
    """

    def setUp(self):
        """Set up schema definitions for validation."""
        self.config_path = '.coderabbit.yaml'
        
        # Define expected schema structure
        self.schema = {
            'language': {'type': str, 'required': True},
            'early_access': {'type': bool, 'required': True},
            'reviews': {
                'type': dict,
                'required': True,
                'schema': {
                    'profile': {'type': str, 'required': True, 'allowed': ['chill', 'assertive', 'pythonic']},
                    'request_changes_workflow': {'type': bool, 'required': True},
                    'high_level_summary': {'type': bool, 'required': True},
                    'poem': {'type': bool, 'required': True},
                    'review_status': {'type': bool, 'required': True},
                    'collapse_walkthrough': {'type': bool, 'required': True},
                    'auto_review': {
                        'type': dict,
                        'required': True,
                        'schema': {
                            'enabled': {'type': bool, 'required': True},
                            'drafts': {'type': bool, 'required': True},
                            'target_branches': {'type': list, 'required': True, 'min_length': 1}
                        }
                    }
                }
            },
            'chat': {
                'type': dict,
                'required': True,
                'schema': {
                    'auto_reply': {'type': bool, 'required': True}
                }
            }
        }

    def validate_field(self, field_name: str, value: Any, field_schema: Dict) -> List[str]:
        """Validate a single field against its schema."""
        errors = []
        
        # Check type
        expected_type = field_schema.get('type')
        if expected_type and not isinstance(value, expected_type):
            errors.append(f"Field '{field_name}' should be of type {expected_type.__name__}, got {type(value).__name__}")
        
        # Check allowed values
        allowed_values = field_schema.get('allowed')
        if allowed_values and value not in allowed_values:
            errors.append(f"Field '{field_name}' value '{value}' not in allowed values: {allowed_values}")
        
        # Check minimum length for lists
        min_length = field_schema.get('min_length')
        if min_length and isinstance(value, list) and len(value) < min_length:
            errors.append(f"Field '{field_name}' should have at least {min_length} items, got {len(value)}")
        
        # Recursively validate nested schemas
        nested_schema = field_schema.get('schema')
        if nested_schema and isinstance(value, dict):
            errors.extend(self.validate_schema(value, nested_schema, field_name))
        
        return errors

    def validate_schema(self, config: Dict, schema: Dict, parent_path: str = '') -> List[str]:
        """Validate configuration against schema."""
        errors = []
        
        # Check required fields
        for field_name, field_schema in schema.items():
            full_field_name = f"{parent_path}.{field_name}" if parent_path else field_name
            
            if field_schema.get('required', False) and field_name not in config:
                errors.append(f"Required field '{full_field_name}' is missing")
                continue
            
            if field_name in config:
                field_errors = self.validate_field(full_field_name, config[field_name], field_schema)
                errors.extend(field_errors)
        
        # Check for unexpected fields
        expected_fields = set(schema.keys())
        actual_fields = set(config.keys())
        unexpected_fields = actual_fields - expected_fields
        
        if unexpected_fields:
            parent_desc = f" in '{parent_path}'" if parent_path else ""
            errors.append(f"Unexpected fields{parent_desc}: {list(unexpected_fields)}")
        
        return errors

    def test_schema_validation_passes(self):
        """Test that the YAML configuration passes schema validation."""
        if not os.path.exists(self.config_path):
            self.skipTest(f"Configuration file {self.config_path} not found")
        
        with open(self.config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        validation_errors = self.validate_schema(config, self.schema)
        
        if validation_errors:
            error_message = "Schema validation failed:\n" + "\n".join(f"  - {error}" for error in validation_errors)
            self.fail(error_message)

    def test_language_code_format(self):
        """Test that language code follows proper locale format."""
        if not os.path.exists(self.config_path):
            self.skipTest(f"Configuration file {self.config_path} not found")
        
        with open(self.config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        language = config.get('language', '')
        
        # Test locale format (language-COUNTRY)
        import re
        locale_pattern = re.compile(r'^[a-z]{2}-[A-Z]{2}$')
        self.assertTrue(
            locale_pattern.match(language),
            f"Language '{language}' should follow locale format (e.g., 'ko-KR', 'en-US')"
        )

    def test_target_branches_are_valid_git_refs(self):
        """Test that target branches are valid Git reference names."""
        if not os.path.exists(self.config_path):
            self.skipTest(f"Configuration file {self.config_path} not found")
        
        with open(self.config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        target_branches = config.get('reviews', {}).get('auto_review', {}).get('target_branches', [])
        
        # Git reference name validation (simplified)
        import re
        valid_ref_pattern = re.compile(r'^[a-zA-Z0-9][a-zA-Z0-9._/-]*[a-zA-Z0-9]$|^[a-zA-Z0-9]$')
        
        for branch in target_branches:
            with self.subTest(branch=branch):
                self.assertTrue(
                    valid_ref_pattern.match(branch),
                    f"Branch '{branch}' is not a valid Git reference name"
                )
                
                # Additional Git ref rules
                self.assertNotIn('..', branch, f"Branch '{branch}' should not contain '..'")
                self.assertFalse(branch.startswith('/'), f"Branch '{branch}' should not start with '/'")
                self.assertFalse(branch.endswith('/'), f"Branch '{branch}' should not end with '/'")
                self.assertNotIn('//', branch, f"Branch '{branch}' should not contain '//'")

    def test_configuration_consistency(self):
        """Test internal consistency of configuration values."""
        if not os.path.exists(self.config_path):
            self.skipTest(f"Configuration file {self.config_path} not found")
        
        with open(self.config_path, 'r', encoding='utf-8') as f:
            config = yaml.safe_load(f)
        
        reviews = config.get('reviews', {})
        auto_review = reviews.get('auto_review', {})
        
        # If auto_review is enabled but drafts is also enabled, that might be inconsistent
        if auto_review.get('enabled', False) and auto_review.get('drafts', False):
            # This might be intentional, so just warn in comments rather than fail
            pass  # Could add warning logic here
        
        # If request_changes_workflow is enabled, review_status should probably be enabled
        if reviews.get('request_changes_workflow', False):
            self.assertTrue(
                reviews.get('review_status', False),
                "When request_changes_workflow is enabled, review_status should typically be enabled too"
            )


if __name__ == '__main__':
    unittest.main()