<#ftl strip_whitespace=true>
<#macro main>
    <#setting number_format="0.#######">
    <#setting url_escaping_charset='UTF-8'>

  <!doctype html>
  <html>
  <head>
    <meta charset="utf-8">
    <title>Messaging API Demo site</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
          crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
      <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
              integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
              crossorigin="anonymous"></script>
      <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
              integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
              crossorigin="anonymous"></script>
  </head>
  <body>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="/">Messaging API Demo</a>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
              <li class="nav-item active">
                  <a class="nav-link" href="/">Home</a>
              </li>
              <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                     data-toggle="dropdown"
                     aria-haspopup="true" aria-expanded="false">
                      Message
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                      <a class="dropdown-item" href="/message/push">Push</a>
                      <a class="dropdown-item" href="/message/broadcast">Broadcast</a>
                      <a class="dropdown-item" href="/message/multicast">Multicast</a>
                      <a class="dropdown-item" href="/message/narrowcast">Narrowcast</a>
                      <div class="dropdown-divider"></div>
                      <a class="dropdown-item" href="/message/report/quota">Quota report</a>
                      <a class="dropdown-item" href="/message/report/sent">Sent report</a>
                  </div>
              </li>
              <li>
                  <a class="nav-link" href="/manage_audience/">Manage audience</a>
              </li>
              <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownBot" role="button"
                     data-toggle="dropdown"
                     aria-haspopup="true" aria-expanded="false">
                      Bot Configuration
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdownBot">
                      <a class="dropdown-item" href="/bot/info">Basic Info</a>
                      <div class="dropdown-divider"></div>
                      <a class="dropdown-item" href="/bot/webhook">Webhook</a>
                  </div>
              </li>
              <li class="nav-item active">
                  <a class="nav-link" href="/request_log/">HTTP Request Logs</a>
              </li>
          </ul>
      </div>
  </nav>


  <div class="container">
      <#nested/>
  </div>

  </body>
  </html>
</#macro>
