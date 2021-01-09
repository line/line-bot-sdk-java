<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Webhook</h1>

    <h2>Webhook information</h2>

    <#if notFoundException??>
        <p>This bot doesn't have an webhook endpoint: ${notFoundException.message}</p>
    <#else>
        <table class="table table-bordered">
            <tr>
                <th>endpoint</th>
                <td>${webhook.endpoint}</td>
            </tr>
            <tr>
                <th>active</th>
                <td>${webhook.active?c}</td>
            </tr>
        </table>
    </#if>

    <h2>Set webhook</h2>
    <form action="/bot/set_webhook" method="post">
        <table>
            <tr>
                <th><label for="webhookTestUrl">URL</label></th>
                <td><input id="webhookTestUrl" type="url" name="url" required>
                    <button type="submit">Set</button>
                </td>
            </tr>
        </table>
    </form>

    <h2>Test webhook</h2>
    <form method="post" action="/bot/test_webhook">
        <table>
            <tr>
                <th><label for="webhookTestUrl">URL</label></th>
                <td><input id="webhookTestUrl" type="url" name="url">(Optional)
                    <button type="submit">Test</button>
                </td>
            </tr>
        </table>
    </form>

</@wrapper.main>
