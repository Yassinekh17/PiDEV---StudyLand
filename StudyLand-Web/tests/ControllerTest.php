<?php

use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;

class ControllerTest extends WebTestCase
{
    public function testSuccessfulAuthentication()
    {
        $client = static::createClient();

        // Simulate a successful authentication attempt with valid credentials of a Formateur user
        $crawler = $client->request('POST', '/login', [ // Update '/login' to the correct URL
            'email' => 'adel@gmail.com',
            'password' => '123456',
        ]);

        // Assert that the response redirects to the dashboard
        $this->assertResponseRedirects('/dash');
    }

    public function testFailedAuthentication()
    {
        $client = static::createClient();

        // Simulate a failed authentication attempt with invalid credentials
        $crawler = $client->request('POST', '/login', [ // Update '/login' to the correct URL
            'email' => 'nonexistent@example.com',
            'password' => 'wrong_password',
        ]);

        // Assert that the response redirects back to the login page
        $this->assertResponseRedirects('/login');
    }

}
