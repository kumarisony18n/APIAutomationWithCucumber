@Put
Feature: feature to update Userskill mappings

  Scenario Outline: Put request to update existing User details with valid inputs
    Given LMS API is up and running for authorized user
    When User sends the Put request to modify the Userskill record with valid inputs with endpoints as "/UserSkills/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then record is successfully updated with success status code

    Examples: 
      | SheetName | RowNumber |
      | Put    |         0 |

  
