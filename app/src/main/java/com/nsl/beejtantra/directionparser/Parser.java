package com.nsl.beejtantra.directionparser;


import java.util.List;

//. by Haseem Saheed
public interface Parser {
    List<Route> parse() throws RouteException;
}