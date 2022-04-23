@Get
Feature: feature to get Userskill mappings for authorized user

  Scenario: Get all UserSkill mappings
    Given LMS API is up and running for authorized user
    When User sends the Get request with endpoints as "/UserSkills"
    Then API should return all Userskill mappings with success status code

  Scenario Outline: Get mapping for specific userskill
    Given LMS API is up and running for authorized user
    When User sends the Get request for specific userskill with endpoints as "/UserSkills/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then API should return specific userskill mapping with success status code

    Examples: 
      | SheetName | RowNumber |
      | Get       |         0 |
