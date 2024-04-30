[#assign PurlUtils = statics['org.ossreviewtoolkit.model.utils.PurlExtensionsKt']]

:publisher: OSS Review Toolkit
[#assign now = .now]

:title-page:
:sectnums:
:toc:

== Packages
[#assign advisorResults = helper.advisorResultsWithVulnerabilities()]

[#list advisorResults as id, results]
=== ${PurlUtils.toPurl(id)}

[#list results as result]

*Advisor: ${result.advisor.name}*
[#assign total = 0]
[#list helper.filterForUnresolvedVulnerabilities(result.vulnerabilities) as vulnerability]

* ${vulnerability.id}
[#list vulnerability.references?filter(ref -> ref.scoringSystem?? && ref.severity??) as reference]
[#assign severityString = vulnerabilityReference.getSeverityString(reference.scoringSystem, reference.severity)]
[#if reference.severityRating == "MEDIUM"]
[#assign total = total + 1]
[/#if]
** Source: ${reference.url} +
   Severity: [.severity-${severityString?lower_case}]#${reference.severity}# (${reference.scoringSystem})

[/#list]

[/#list]
* MEDIUM: ${total}
[#list result.summary.issues as issue]
* ${issue.severity}: ${issue.message}
[/#list]

[/#list]
[/#list]