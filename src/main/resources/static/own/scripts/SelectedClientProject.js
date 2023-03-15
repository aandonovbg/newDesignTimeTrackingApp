function getProjectsForClient(clientId) {
  console.log('Getting projects for client with ID:'+clientId);
  $.ajax({
    url: '/protocol/projectsList/' + clientId,
    type: 'GET',
    success: function (data) {
      var projectDropdown = $('#project');
      projectDropdown.empty();
      $.each(data, function (i, project) {
        $('<option/>').val(project.id).text(project.projectName).appendTo(projectDropdown);
      });
      projectDropdown.prop('disabled', false);
      projectDropdown.selectpicker('refresh');
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log('Error getting projects:', textStatus);
    }
  });
}''

$(document).ready(function() {
    $('#client').on('change', function() {
        var clientId = $(this).val();
        if(clientId) {
            getProjectsForClient(clientId);
        }
    });
});