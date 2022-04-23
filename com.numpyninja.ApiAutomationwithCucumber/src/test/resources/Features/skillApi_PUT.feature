@Put
Feature: Check Put request for existing skill details

  Scenario Outline: Put request to update existing skill details with valid inputs
    Given LMS API is up and running for authorized user
    When User sends Put request for Skill Api with endpoint as "/Skills/{id}" and from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then Skill record is successfully updated with success status code

    Examples: 
      | SheetName | RowNumber |
      | Put    |         0 |
      | Put    |         1 |
