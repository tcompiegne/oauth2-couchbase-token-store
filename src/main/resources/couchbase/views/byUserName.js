// @author Titouan COMPIEGNE

// Create this view inside "oAuth2AccessToken" design document
// View name : byUserName

function (doc, meta) {
  if(doc._class == "com.tce.spring.oauth2.domain.CouchbaseOAuth2AccessToken" && doc.userName) {
    emit(doc.userName, null);
  }
}