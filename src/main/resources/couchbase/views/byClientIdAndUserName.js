// @author Titouan COMPIEGNE

// Create this view inside "oAuth2AccessToken" design document
// View name : byClientIdAndUserName

function(doc, meta) {
	if(doc._class == "com.tce.spring.oauth2.domain.CouchbaseOAuth2AccessToken" && doc.clientId && doc.userName) {
		emit([doc.clientId, doc.userName], null);
	}
}