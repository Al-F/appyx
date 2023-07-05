import subprocess
import shutil
import os
from urllib.parse import urljoin, urlparse


def compile_project(compile_task):
    """Compile a project using Gradle"""
    process = subprocess.Popen(args=['./gradlew', compile_task],
                               stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE)
    process.communicate()
    if process.returncode != 0:
        raise ChildProcessError


def copy_files(source_directory, target_directory):
    """Copy files from one directory to another, preserving metadata"""
    shutil.copytree(source_directory, target_directory, copy_function=shutil.copy2)


def is_running_on_ci():
    os.environ.get('GITHUB_ACTIONS') == 'false'

def generate_html(width, height, target_directory, html_file_name, classname):
    """Generate HTML code"""
    return "<div class=\"{classname}\">" \
           "<iframe " \
           "width={width} " \
           "height={height} " \
           "frameBorder=0 " \
           "scrolling=\"no\" " \
           "src=\"{target_directory}/{html_file_name}\">" \
           "</iframe>"\
           "</div>".format(
                width=width,
                height=height,
                target_directory=target_directory,
                html_file_name=html_file_name,
                classname=classname,
           )


def define_env(env):
    """Compile, copy and generate HTML tag needed for embedding Compose Multiplatform content"""

    @env.macro
    def compose_mpp_sample(
            project_output_directory,
            compile_task,
            width,
            height,
            target_directory,
            html_file_name,
            classname,
    ):
        if not os.path.exists(project_output_directory):
            compile_project(compile_task)
        site_target_directory = os.path.join(env.variables.config.site_dir, target_directory)
        if not os.path.exists(site_target_directory):
            copy_files(project_output_directory, site_target_directory)
        base_url = '/'
        html_target_directory = urljoin(base_url, os.path.relpath(site_target_directory, env.variables.config.site_dir))
        return generate_html(width, height, html_target_directory, html_file_name, classname)
