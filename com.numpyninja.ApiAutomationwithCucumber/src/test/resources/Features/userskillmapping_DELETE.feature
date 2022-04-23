@Delete
Feature: feature to delete Userskill mappings

  Scenario Outline: Delete an existing User-Skill mapping detail
    Given LMS API is up and running for authorized user
    When User sends the Delete request with valid inputs with endpoints as "/UserSkills/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then record is successfully deleted with success status code
     Examples: 
      | SheetName | RowNumber |
      | Delete    |         0 |
      

