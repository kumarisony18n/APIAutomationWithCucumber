@Delete
Feature: Check Delete request to delete an existing Skill detail

  Scenario Outline: Delete request to delete existing skill with valid input
    Given LMS API is up and running for authorized user
    When User sends the Delete request to SkillApi with valid input and endpoints as "/Skills/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then skill record is successfully deleted with success status code
    Examples: 
      | SheetName | RowNumber |
      | Delete    |         0 |

