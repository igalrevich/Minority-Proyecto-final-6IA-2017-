using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Web.Http;

namespace RestApiMinority
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Web API configuration and services

            // Web API routes
            config.MapHttpAttributeRoutes();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );

            config.Routes.MapHttpRoute(
              name: "GetIdByNombre", // Route name
              routeTemplate: "api/{controller}/{action}/{tabla}/{nombre}",// URL with parameters
              defaults: new { nombre = "", tabla = "" }  // Parameter defaults
              );
            config.Routes.MapHttpRoute(
              name: "GetCantVotos", // Route name
              routeTemplate: "api/{controller}/{action}/{Opcion}",// URL with parameters
              defaults: new { Opcion = "" }  // Parameter defaults
              );
            



            config.Formatters.JsonFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("text/html"));
        }
    }
}
