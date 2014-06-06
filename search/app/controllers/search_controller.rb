class SearchController < ApplicationController

  def form
    respond_to do |format|
      #format.html { render :text => Person.where(:first_name => 'chris').group(:country).order('count_all desc').limit(5).count }
      format.html { render 'search/form' }
    end
  end

  def search
    if params[:first_name].present?
      @first_name_term = params[:first_name].downcase
      @first_name_result = Person.where(:first_name => @first_name_term).group(:country).order('count_all desc').limit(5).count.keys.join(", ")
    end

    if params[:second_name].present?
      @second_name_term = params[:second_name].downcase
      @second_name_result = Person.where(:second_name => @second_name_term).group(:country).order('count_all desc').limit(5).count.keys.join(", ")
    end

    if @first_name_term and @second_name_term
      @full_name_term = "#{@first_name_term} #{@second_name_term}"
      @full_name_result = Person.where({:first_name => @first_name_term, :second_name => @second_name_term}).group(:country).order('count_all desc').limit(5).count.keys.join(", ")
    end

    render 'search/result'
  end

end
