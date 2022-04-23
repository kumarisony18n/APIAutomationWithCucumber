@Get
Feature: Check the Get request to fetch all UserSkillMapGetApi data

  Scenario: Get request to fetch List of all users with all skill details
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch List of all users with all skill details with endpoint as "/UserSkillsMap"
    Then API should return List of all users with all skill details with success status code

  Scenario Outline: Get request to fetch List of user with the skill details by USER_ID
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch List of user with the skill details by USER_ID with endpoint as "/UserSkillsMap/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then API should return List of user with the skill details by USER_ID with success status code

    Examples: 
      | SheetName   | RowNumber |
      | GetByUserid |         0 |

  Scenario Outline: Get request to fetch List of all users details by SKILL_ID
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch List of all users details by SKILL_ID with endpoint as "/UsersSkillsMap/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then API should return List of all users details by SKILL_ID with success status code

    Examples: 
      | SheetName    | RowNumber |
      | GetBySkillId |         0 |
