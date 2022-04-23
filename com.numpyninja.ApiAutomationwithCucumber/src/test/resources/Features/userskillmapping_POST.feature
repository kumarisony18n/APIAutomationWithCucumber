@Post
Feature: feature to create Userskill mappings

  Scenario Outline: Post request to create new Userskill mapping
    Given LMS API is up and running for authorized user
    When User sends the Post request for new Userskill mapping with valid inputs from given sheetname "<SheetName>" and rownumber <RowNumber> and endpoints as "/UserSkills"
    Then New UsersSkill record is successfully created with success status code

    Examples: 
      | SheetName | RowNumber |
      | Post    |         0 |
     