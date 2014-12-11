import org.grails.cli.interactive.completers.DomainClassCompleter

description( "Generates a controller that performs CRUD operations and the associated views" ) {
  usage "grails generate-all [DOMAIN CLASS]"
  completer DomainClassCompleter
  flag name:'force', description:"Whether to overwrite existing files"
}

if(args) {
  def classNames = args
  if(args[0] == '*') {
    classNames = resources("file:grails-app/domain/**/*.groovy")
                    .collect { className(it) }
  }
  for(arg in classNames) {
    def sourceClass = source(arg)
    def overwrite = flag('force') ? true : false
    if(sourceClass) {
      def model = model(sourceClass)
      render template: template('scaffolding/Controller.groovy'), 
             destination: file("grails-app/controllers/${model.packagePath}/${model.convention('Controller')}.groovy"),
             model: model,
             overwrite: overwrite

      render template: template('scaffolding/Spec.groovy'), 
             destination: file("src/test/groovy/${model.packagePath}/${model.convention('ControllerSpec')}.groovy"),
             model: model,
             overwrite: overwrite

      render template: template('scaffolding/edit.gsp'), 
             destination: file("grails-app/views/${model.propertyName}/edit.gsp"),
             model: model,
             overwrite: overwrite

      render template: template('scaffolding/create.gsp'), 
             destination: file("grails-app/views/${model.propertyName}/create.gsp"),
             model: model,
             overwrite: overwrite

      render template: template('scaffolding/index.gsp'), 
             destination: file("grails-app/views/${model.propertyName}/index.gsp"),
             model: model,
             overwrite: overwrite

      render template: template('scaffolding/show.gsp'), 
             destination: file("grails-app/views/${model.propertyName}/show.gsp"),
             model: model,
             overwrite: overwrite


      addStatus "Scaffolding completed for ${projectPath(sourceClass)}"                                         
    }
    else {
      error "Domain class not found for name $arg"
    }
  }
}
else {
    error "No domain class specified"
}
