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
            
             config.Routes.MapHttpRoute(
                name: "GetIdSala",
                routeTemplate: "api/{controller}/{action}/{NombreSala}",
                defaults: new { action="GetIdSala"}
            );
            
            
           
>>>>>>> 3426420507a04fa2500c36936222d56e67cb6d45

            config.Formatters.JsonFormatter.SupportedMediaTypes
                .Add(new MediaTypeHeaderValue("text/html"));
        }
    }
}
