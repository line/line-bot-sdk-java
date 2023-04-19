<#-- @ftlvariable name="logs" type="java.util.List<com.linecorp.bot.messagingapidemoapp.controller.RequestLogController.ApiCallLog>" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Request log</h1>

    <#list logs as log>
        <div class="row">
            <div class="col-sm-6">
                <p>${log.request.method} ${log.request.url}</p>
                <table class="table table-hover">
                    <#list log.request.headers as header>
                        <tr>
                            <th>${header.name}</th>
                            <td>${header.value}</td>
                        </tr>
                    </#list>
                </table>
                <pre><code id="requestLog${log?index}">${log.request.body!"-"}</code></pre>
                <button class="prettify-btn bnt" data-target="requestLog${log?index}">Prettify</button>
            </div>
            <div class="col-sm-6">
                <p>${log.response.code} ${log.response.message}</p>
                <table class="table table-hover">
                    <#list log.response.headers as header>
                        <tr>
                            <th>${header.name}</th>
                            <td>${header.value}</td>
                        </tr>
                    </#list>
                </table>
                <pre><code id="responseLog${log?index}">${log.response.body!"-"}</code></pre>
                <button class="prettify-btn bnt" data-target="responseLog${log?index}">Prettify</button>
            </div>
        </div>
        <hr>
    </#list>

    <style>
        td {
            word-break: break-all;
        }
    </style>

    <script>
      const elems = document.getElementsByClassName("prettify-btn")
      for (let i = 0; i < elems.length; i++) {
        let idx = i;
        elems[idx].addEventListener('click', e => {
          const target = elems[idx].getAttribute("data-target");
          const targetElement = document.getElementById(target);
          const src = targetElement.innerText;
          targetElement.innerText = JSON.stringify(JSON.parse(src), null, 2);
        });
      }
    </script>

</@wrapper.main>
