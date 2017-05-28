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
<<<<<<< HEAD


            config.Routes.MapHttpRoute(
               name: "GetIdSala",
               routeTemplate: "api/rest/GetIdSala/{nombreSala}");  
=======
            
            
>>>>>>> 3e9c0ab538e5b0610e7cac3603ffcfe9ea17ec0b

            config.Formatters.JsonFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("text/html"));
        }
    }
}
